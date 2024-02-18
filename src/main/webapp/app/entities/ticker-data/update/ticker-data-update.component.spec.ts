import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ITicker } from 'app/entities/ticker/ticker.model';
import { TickerService } from 'app/entities/ticker/service/ticker.service';
import { TickerDataService } from '../service/ticker-data.service';
import { ITickerData } from '../ticker-data.model';
import { TickerDataFormService } from './ticker-data-form.service';

import { TickerDataUpdateComponent } from './ticker-data-update.component';

describe('TickerData Management Update Component', () => {
  let comp: TickerDataUpdateComponent;
  let fixture: ComponentFixture<TickerDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tickerDataFormService: TickerDataFormService;
  let tickerDataService: TickerDataService;
  let tickerService: TickerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TickerDataUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TickerDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TickerDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tickerDataFormService = TestBed.inject(TickerDataFormService);
    tickerDataService = TestBed.inject(TickerDataService);
    tickerService = TestBed.inject(TickerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Ticker query and add missing value', () => {
      const tickerData: ITickerData = { id: 456 };
      const ticker: ITicker = { id: 792 };
      tickerData.ticker = ticker;

      const tickerCollection: ITicker[] = [{ id: 2817 }];
      jest.spyOn(tickerService, 'query').mockReturnValue(of(new HttpResponse({ body: tickerCollection })));
      const additionalTickers = [ticker];
      const expectedCollection: ITicker[] = [...additionalTickers, ...tickerCollection];
      jest.spyOn(tickerService, 'addTickerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tickerData });
      comp.ngOnInit();

      expect(tickerService.query).toHaveBeenCalled();
      expect(tickerService.addTickerToCollectionIfMissing).toHaveBeenCalledWith(
        tickerCollection,
        ...additionalTickers.map(expect.objectContaining),
      );
      expect(comp.tickersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tickerData: ITickerData = { id: 456 };
      const ticker: ITicker = { id: 3768 };
      tickerData.ticker = ticker;

      activatedRoute.data = of({ tickerData });
      comp.ngOnInit();

      expect(comp.tickersSharedCollection).toContain(ticker);
      expect(comp.tickerData).toEqual(tickerData);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITickerData>>();
      const tickerData = { id: 123 };
      jest.spyOn(tickerDataFormService, 'getTickerData').mockReturnValue(tickerData);
      jest.spyOn(tickerDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tickerData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tickerData }));
      saveSubject.complete();

      // THEN
      expect(tickerDataFormService.getTickerData).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tickerDataService.update).toHaveBeenCalledWith(expect.objectContaining(tickerData));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITickerData>>();
      const tickerData = { id: 123 };
      jest.spyOn(tickerDataFormService, 'getTickerData').mockReturnValue({ id: null });
      jest.spyOn(tickerDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tickerData: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tickerData }));
      saveSubject.complete();

      // THEN
      expect(tickerDataFormService.getTickerData).toHaveBeenCalled();
      expect(tickerDataService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITickerData>>();
      const tickerData = { id: 123 };
      jest.spyOn(tickerDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tickerData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tickerDataService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTicker', () => {
      it('Should forward to tickerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tickerService, 'compareTicker');
        comp.compareTicker(entity, entity2);
        expect(tickerService.compareTicker).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
