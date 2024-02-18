import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITicker } from 'app/entities/ticker/ticker.model';
import { TickerService } from 'app/entities/ticker/service/ticker.service';
import { ITickerData } from '../ticker-data.model';
import { TickerDataService } from '../service/ticker-data.service';
import { TickerDataFormService, TickerDataFormGroup } from './ticker-data-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ticker-data-update',
  templateUrl: './ticker-data-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TickerDataUpdateComponent implements OnInit {
  isSaving = false;
  tickerData: ITickerData | null = null;

  tickersSharedCollection: ITicker[] = [];

  editForm: TickerDataFormGroup = this.tickerDataFormService.createTickerDataFormGroup();

  constructor(
    protected tickerDataService: TickerDataService,
    protected tickerDataFormService: TickerDataFormService,
    protected tickerService: TickerService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareTicker = (o1: ITicker | null, o2: ITicker | null): boolean => this.tickerService.compareTicker(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tickerData }) => {
      this.tickerData = tickerData;
      if (tickerData) {
        this.updateForm(tickerData);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tickerData = this.tickerDataFormService.getTickerData(this.editForm);
    if (tickerData.id !== null) {
      this.subscribeToSaveResponse(this.tickerDataService.update(tickerData));
    } else {
      this.subscribeToSaveResponse(this.tickerDataService.create(tickerData));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITickerData>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tickerData: ITickerData): void {
    this.tickerData = tickerData;
    this.tickerDataFormService.resetForm(this.editForm, tickerData);

    this.tickersSharedCollection = this.tickerService.addTickerToCollectionIfMissing<ITicker>(
      this.tickersSharedCollection,
      tickerData.ticker,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tickerService
      .query()
      .pipe(map((res: HttpResponse<ITicker[]>) => res.body ?? []))
      .pipe(map((tickers: ITicker[]) => this.tickerService.addTickerToCollectionIfMissing<ITicker>(tickers, this.tickerData?.ticker)))
      .subscribe((tickers: ITicker[]) => (this.tickersSharedCollection = tickers));
  }
}
