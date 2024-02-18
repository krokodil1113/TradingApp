import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ticker-data.test-samples';

import { TickerDataFormService } from './ticker-data-form.service';

describe('TickerData Form Service', () => {
  let service: TickerDataFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TickerDataFormService);
  });

  describe('Service methods', () => {
    describe('createTickerDataFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTickerDataFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            adjClose: expect.any(Object),
            close: expect.any(Object),
            high: expect.any(Object),
            low: expect.any(Object),
            open: expect.any(Object),
            volume: expect.any(Object),
            ticker: expect.any(Object),
          }),
        );
      });

      it('passing ITickerData should create a new form with FormGroup', () => {
        const formGroup = service.createTickerDataFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            adjClose: expect.any(Object),
            close: expect.any(Object),
            high: expect.any(Object),
            low: expect.any(Object),
            open: expect.any(Object),
            volume: expect.any(Object),
            ticker: expect.any(Object),
          }),
        );
      });
    });

    describe('getTickerData', () => {
      it('should return NewTickerData for default TickerData initial value', () => {
        const formGroup = service.createTickerDataFormGroup(sampleWithNewData);

        const tickerData = service.getTickerData(formGroup) as any;

        expect(tickerData).toMatchObject(sampleWithNewData);
      });

      it('should return NewTickerData for empty TickerData initial value', () => {
        const formGroup = service.createTickerDataFormGroup();

        const tickerData = service.getTickerData(formGroup) as any;

        expect(tickerData).toMatchObject({});
      });

      it('should return ITickerData', () => {
        const formGroup = service.createTickerDataFormGroup(sampleWithRequiredData);

        const tickerData = service.getTickerData(formGroup) as any;

        expect(tickerData).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITickerData should not enable id FormControl', () => {
        const formGroup = service.createTickerDataFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTickerData should disable id FormControl', () => {
        const formGroup = service.createTickerDataFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
