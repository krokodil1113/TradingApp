import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITickerData } from '../ticker-data.model';
import { TickerDataService } from '../service/ticker-data.service';

@Component({
  standalone: true,
  templateUrl: './ticker-data-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TickerDataDeleteDialogComponent {
  tickerData?: ITickerData;

  constructor(
    protected tickerDataService: TickerDataService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tickerDataService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
