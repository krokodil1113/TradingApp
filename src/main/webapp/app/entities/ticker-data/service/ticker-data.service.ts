import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITickerData, NewTickerData } from '../ticker-data.model';

export type PartialUpdateTickerData = Partial<ITickerData> & Pick<ITickerData, 'id'>;

export type EntityResponseType = HttpResponse<ITickerData>;
export type EntityArrayResponseType = HttpResponse<ITickerData[]>;

@Injectable({ providedIn: 'root' })
export class TickerDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ticker-data');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(tickerData: NewTickerData): Observable<EntityResponseType> {
    return this.http.post<ITickerData>(this.resourceUrl, tickerData, { observe: 'response' });
  }

  update(tickerData: ITickerData): Observable<EntityResponseType> {
    return this.http.put<ITickerData>(`${this.resourceUrl}/${this.getTickerDataIdentifier(tickerData)}`, tickerData, {
      observe: 'response',
    });
  }

  partialUpdate(tickerData: PartialUpdateTickerData): Observable<EntityResponseType> {
    return this.http.patch<ITickerData>(`${this.resourceUrl}/${this.getTickerDataIdentifier(tickerData)}`, tickerData, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITickerData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITickerData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTickerDataIdentifier(tickerData: Pick<ITickerData, 'id'>): number {
    return tickerData.id ?? 0;
  }

  compareTickerData(o1: Pick<ITickerData, 'id'> | null, o2: Pick<ITickerData, 'id'> | null): boolean {
    return o1 && o2 ? this.getTickerDataIdentifier(o1) === this.getTickerDataIdentifier(o2) : o1 === o2;
  }

  addTickerDataToCollectionIfMissing<Type extends Pick<ITickerData, 'id'>>(
    tickerDataCollection: Type[],
    ...tickerDataToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tickerData: Type[] = tickerDataToCheck.filter(isPresent);
    if (tickerData.length > 0) {
      const tickerDataCollectionIdentifiers = tickerDataCollection.map(tickerDataItem => this.getTickerDataIdentifier(tickerDataItem)!);
      const tickerDataToAdd = tickerData.filter(tickerDataItem => {
        const tickerDataIdentifier = this.getTickerDataIdentifier(tickerDataItem);
        if (tickerDataCollectionIdentifiers.includes(tickerDataIdentifier)) {
          return false;
        }
        tickerDataCollectionIdentifiers.push(tickerDataIdentifier);
        return true;
      });
      return [...tickerDataToAdd, ...tickerDataCollection];
    }
    return tickerDataCollection;
  }
}
