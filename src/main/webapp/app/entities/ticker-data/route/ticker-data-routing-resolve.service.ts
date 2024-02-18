import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITickerData } from '../ticker-data.model';
import { TickerDataService } from '../service/ticker-data.service';

export const tickerDataResolve = (route: ActivatedRouteSnapshot): Observable<null | ITickerData> => {
  const id = route.params['id'];
  if (id) {
    return inject(TickerDataService)
      .find(id)
      .pipe(
        mergeMap((tickerData: HttpResponse<ITickerData>) => {
          if (tickerData.body) {
            return of(tickerData.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default tickerDataResolve;
