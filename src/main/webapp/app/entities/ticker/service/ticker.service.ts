import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITicker, NewTicker } from '../ticker.model';

export type PartialUpdateTicker = Partial<ITicker> & Pick<ITicker, 'id'>;

export type EntityResponseType = HttpResponse<ITicker>;
export type EntityArrayResponseType = HttpResponse<ITicker[]>;

@Injectable({ providedIn: 'root' })
export class TickerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tickers');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  //************************************************* */
  fetchTickerData(symbol: any): Observable<any> {
    // Adjusted mock data to match the structure you provided
    const mockData = {
      '2022-01-03T00:00:00.000': {
        'Adj Close': 0.1711000055,
        Close: 0.1711000055,
        High: 0.1711000055,
        Low: 0.1711000055,
        Open: 0.1711000055,
        Volume: 0,
      },
      '2022-01-04T00:00:00.000': {
        'Adj Close': 0.1711000055,
        Close: 0.1711000055,
        High: 0.1711000055,
        Low: 0.1711000055,
        Open: 0.1711000055,
        Volume: 0,
      },
    };

    return of(mockData); // Return the adjusted mock data as an Observable
  }
  //************************************************* */

  create(ticker: NewTicker): Observable<EntityResponseType> {
    return this.http.post<ITicker>(this.resourceUrl, ticker, { observe: 'response' });
  }

  update(ticker: ITicker): Observable<EntityResponseType> {
    return this.http.put<ITicker>(`${this.resourceUrl}/${this.getTickerIdentifier(ticker)}`, ticker, { observe: 'response' });
  }

  partialUpdate(ticker: PartialUpdateTicker): Observable<EntityResponseType> {
    return this.http.patch<ITicker>(`${this.resourceUrl}/${this.getTickerIdentifier(ticker)}`, ticker, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITicker>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    console.log('******************THIS IS TickerService');
    const options = createRequestOption(req);
    return this.http.get<ITicker[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTickerIdentifier(ticker: Pick<ITicker, 'id'>): number {
    return ticker.id;
  }

  compareTicker(o1: Pick<ITicker, 'id'> | null, o2: Pick<ITicker, 'id'> | null): boolean {
    return o1 && o2 ? this.getTickerIdentifier(o1) === this.getTickerIdentifier(o2) : o1 === o2;
  }

  addTickerToCollectionIfMissing<Type extends Pick<ITicker, 'id'>>(
    tickerCollection: Type[],
    ...tickersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tickers: Type[] = tickersToCheck.filter(isPresent);
    if (tickers.length > 0) {
      const tickerCollectionIdentifiers = tickerCollection.map(tickerItem => this.getTickerIdentifier(tickerItem)!);
      const tickersToAdd = tickers.filter(tickerItem => {
        const tickerIdentifier = this.getTickerIdentifier(tickerItem);
        if (tickerCollectionIdentifiers.includes(tickerIdentifier)) {
          return false;
        }
        tickerCollectionIdentifiers.push(tickerIdentifier);
        return true;
      });
      return [...tickersToAdd, ...tickerCollection];
    }
    return tickerCollection;
  }
}
