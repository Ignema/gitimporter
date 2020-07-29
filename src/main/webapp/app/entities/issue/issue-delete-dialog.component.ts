import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIssue } from 'app/shared/model/issue.model';
import { IssueService } from './issue.service';

@Component({
  templateUrl: './issue-delete-dialog.component.html',
})
export class IssueDeleteDialogComponent {
  issue?: IIssue;

  constructor(protected issueService: IssueService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.issueService.delete(id).subscribe(() => {
      this.eventManager.broadcast('issueListModification');
      this.activeModal.close();
    });
  }
}
