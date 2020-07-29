import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IGitInfo, GitInfo } from 'app/shared/model/git-info.model';
import { GitInfoService } from './git-info.service';

@Component({
  selector: 'jhi-git-info-update',
  templateUrl: './git-info-update.component.html',
})
export class GitInfoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    gitProjectId: [null, [Validators.required]],
    gitProjectName: [null, [Validators.required]],
    issueCount: [null, [Validators.required]],
  });

  constructor(protected gitInfoService: GitInfoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gitInfo }) => {
      this.updateForm(gitInfo);
    });
  }

  updateForm(gitInfo: IGitInfo): void {
    this.editForm.patchValue({
      id: gitInfo.id,
      gitProjectId: gitInfo.gitProjectId,
      gitProjectName: gitInfo.gitProjectName,
      issueCount: gitInfo.issueCount,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gitInfo = this.createFromForm();
    if (gitInfo.id !== undefined) {
      this.subscribeToSaveResponse(this.gitInfoService.update(gitInfo));
    } else {
      this.subscribeToSaveResponse(this.gitInfoService.create(gitInfo));
    }
  }

  private createFromForm(): IGitInfo {
    return {
      ...new GitInfo(),
      id: this.editForm.get(['id'])!.value,
      gitProjectId: this.editForm.get(['gitProjectId'])!.value,
      gitProjectName: this.editForm.get(['gitProjectName'])!.value,
      issueCount: this.editForm.get(['issueCount'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGitInfo>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
