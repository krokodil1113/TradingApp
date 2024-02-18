import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'ticker',
        data: { pageTitle: 'saIlatestApp.ticker.home.title' },
        loadChildren: () => import('./ticker/ticker.routes'),
      },
      {
        path: 'ticker-data',
        data: { pageTitle: 'saIlatestApp.tickerData.home.title' },
        loadChildren: () => import('./ticker-data/ticker-data.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
