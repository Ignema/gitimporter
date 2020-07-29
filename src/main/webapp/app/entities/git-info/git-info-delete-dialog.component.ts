import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGitInfo } from 'app/shared/model/git-info.model';
import { GitInfoService } from './git-info.service';

@Component({
  templateUrl: './git-info-delete-dialog.component.html',
})
export class GitInfoDeleteDialogComponent {
  gitInfo?: IGitInfo;

  constructor(protected gitInfoService: GitInfoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gitInfoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('gitInfoListModification');
      this.activeModal.close();
    });
  }
}
