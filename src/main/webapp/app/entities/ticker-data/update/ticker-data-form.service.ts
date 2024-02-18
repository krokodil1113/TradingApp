import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITickerData, NewTickerData } from '../ticker-data.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITickerData for edit and NewTickerDataFormGroupInput for create.
 */
type TickerDataFormGroupInput = ITickerData | PartialWithRequiredKeyOf<NewTickerData>;

type TickerDataFormDefaults = Pick<NewTickerData, 'id'>;

type TickerDataFormGroupContent = {
  id: FormControl<ITickerData['id'] | NewTickerData['id']>;
  date: FormControl<ITickerData['date']>;
  adjClose: FormControl<ITickerData['adjClose']>;
  close: FormControl<ITickerData['close']>;
  high: FormControl<ITickerData['high']>;
  low: FormControl<ITickerData['low']>;
  open: FormControl<ITickerData['open']>;
  volume: FormControl<ITickerData['volume']>;
  ticker: FormControl<ITickerData['ticker']>;
};

export type TickerDataFormGroup = FormGroup<TickerDataFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TickerDataFormService {
  createTickerDataFormGroup(tickerData: TickerDataFormGroupInput = { id: null }): TickerDataFormGroup {
    const tickerDataRawValue = {
      ...this.getFormDefaults(),
      ...tickerData,
    };
    return new FormGroup<TickerDataFormGroupContent>({
      id: new FormControl(
        { value: tickerDataRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(tickerDataRawValue.date, {
        validators: [Validators.required],
      }),
      adjClose: new FormControl(tickerDataRawValue.adjClose),
      close: new FormControl(tickerDataRawValue.close),
      high: new FormControl(tickerDataRawValue.high),
      low: new FormControl(tickerDataRawValue.low),
      open: new FormControl(tickerDataRawValue.open),
      volume: new FormControl(tickerDataRawValue.volume),
      ticker: new FormControl(tickerDataRawValue.ticker, {
        validators: [Validators.required],
      }),
    });
  }

  getTickerData(form: TickerDataFormGroup): ITickerData | NewTickerData {
    return form.getRawValue() as ITickerData | NewTickerData;
  }

  resetForm(form: TickerDataFormGroup, tickerData: TickerDataFormGroupInput): void {
    const tickerDataRawValue = { ...this.getFormDefaults(), ...tickerData };
    form.reset(
      {
        ...tickerDataRawValue,
        id: { value: tickerDataRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TickerDataFormDefaults {
    return {
      id: null,
    };
  }
}
