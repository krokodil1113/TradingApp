import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TickerDataDetailComponent } from './ticker-data-detail.component';

describe('TickerData Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TickerDataDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TickerDataDetailComponent,
              resolve: { tickerData: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TickerDataDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load tickerData on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TickerDataDetailComponent);

      // THEN
      expect(instance.tickerData).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
