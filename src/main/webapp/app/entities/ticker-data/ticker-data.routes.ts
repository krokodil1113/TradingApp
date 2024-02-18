import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TickerDataComponent } from './list/ticker-data.component';
import { TickerDataDetailComponent } from './detail/ticker-data-detail.component';
import { TickerDataUpdateComponent } from './update/ticker-data-update.component';
import TickerDataResolve from './route/ticker-data-routing-resolve.service';

const tickerDataRoute: Routes = [
  {
    path: '',
    component: TickerDataComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TickerDataDetailComponent,
    resolve: {
      tickerData: TickerDataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TickerDataUpdateComponent,
    resolve: {
      tickerData: TickerDataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TickerDataUpdateComponent,
    resolve: {
      tickerData: TickerDataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tickerDataRoute;
