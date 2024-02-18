import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITickerData } from '../ticker-data.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ticker-data.test-samples';

import { TickerDataService } from './ticker-data.service';

const requireRestSample: ITickerData = {
  ...sampleWithRequiredData,
};

describe('TickerData Service', () => {
  let service: TickerDataService;
  let httpMock: HttpTestingController;
  let expectedResult: ITickerData | ITickerData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TickerDataService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a TickerData', () => {
      const tickerData = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tickerData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TickerData', () => {
      const tickerData = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tickerData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TickerData', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TickerData', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TickerData', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTickerDataToCollectionIfMissing', () => {
      it('should add a TickerData to an empty array', () => {
        const tickerData: ITickerData = sampleWithRequiredData;
        expectedResult = service.addTickerDataToCollectionIfMissing([], tickerData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tickerData);
      });

      it('should not add a TickerData to an array that contains it', () => {
        const tickerData: ITickerData = sampleWithRequiredData;
        const tickerDataCollection: ITickerData[] = [
          {
            ...tickerData,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTickerDataToCollectionIfMissing(tickerDataCollection, tickerData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TickerData to an array that doesn't contain it", () => {
        const tickerData: ITickerData = sampleWithRequiredData;
        const tickerDataCollection: ITickerData[] = [sampleWithPartialData];
        expectedResult = service.addTickerDataToCollectionIfMissing(tickerDataCollection, tickerData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tickerData);
      });

      it('should add only unique TickerData to an array', () => {
        const tickerDataArray: ITickerData[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tickerDataCollection: ITickerData[] = [sampleWithRequiredData];
        expectedResult = service.addTickerDataToCollectionIfMissing(tickerDataCollection, ...tickerDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tickerData: ITickerData = sampleWithRequiredData;
        const tickerData2: ITickerData = sampleWithPartialData;
        expectedResult = service.addTickerDataToCollectionIfMissing([], tickerData, tickerData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tickerData);
        expect(expectedResult).toContain(tickerData2);
      });

      it('should accept null and undefined values', () => {
        const tickerData: ITickerData = sampleWithRequiredData;
        expectedResult = service.addTickerDataToCollectionIfMissing([], null, tickerData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tickerData);
      });

      it('should return initial array if no TickerData is added', () => {
        const tickerDataCollection: ITickerData[] = [sampleWithRequiredData];
        expectedResult = service.addTickerDataToCollectionIfMissing(tickerDataCollection, undefined, null);
        expect(expectedResult).toEqual(tickerDataCollection);
      });
    });

    describe('compareTickerData', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTickerData(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTickerData(entity1, entity2);
        const compareResult2 = service.compareTickerData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTickerData(entity1, entity2);
        const compareResult2 = service.compareTickerData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTickerData(entity1, entity2);
        const compareResult2 = service.compareTickerData(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
