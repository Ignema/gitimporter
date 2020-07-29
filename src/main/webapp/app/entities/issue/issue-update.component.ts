import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IIssue, Issue } from 'app/shared/model/issue.model';
import { IssueService } from './issue.service';

@Component({
  selector: 'jhi-issue-update',
  templateUrl: './issue-update.component.html',
})
export class IssueUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    issueId: [null, [Validators.required]],
    issueOrder: [null, [Validators.required]],
    issueTitle: [null, [Validators.required]],
    state: [null, [Validators.required]],
    author: [null, [Validators.required]],
    description: [null, [Validators.required]],
    createdAt: [null, [Validators.required]],
    updatedAt: [null, [Validators.required]],
    closedAt: [],
    closedBy: [],
  });

  constructor(protected issueService: IssueService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ issue }) => {
      if (!issue.id) {
        const today = moment().startOf('day');
        issue.createdAt = today;
        issue.updatedAt = today;
        issue.closedAt = today;
      }

      this.updateForm(issue);
    });
  }

  updateForm(issue: IIssue): void {
    this.editForm.patchValue({
      id: issue.id,
      issueId: issue.issueId,
      issueOrder: issue.issueOrder,
      issueTitle: issue.issueTitle,
      state: issue.state,
      author: issue.author,
      description: issue.description,
      createdAt: issue.createdAt ? issue.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: issue.updatedAt ? issue.updatedAt.format(DATE_TIME_FORMAT) : null,
      closedAt: issue.closedAt ? issue.closedAt.format(DATE_TIME_FORMAT) : null,
      closedBy: issue.closedBy,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const issue = this.createFromForm();
    if (issue.id !== undefined) {
      this.subscribeToSaveResponse(this.issueService.update(issue));
    } else {
      this.subscribeToSaveResponse(this.issueService.create(issue));
    }
  }

  private createFromForm(): IIssue {
    return {
      ...new Issue(),
      id: this.editForm.get(['id'])!.value,
      issueId: this.editForm.get(['issueId'])!.value,
      issueOrder: this.editForm.get(['issueOrder'])!.value,
      issueTitle: this.editForm.get(['issueTitle'])!.value,
      state: this.editForm.get(['state'])!.value,
      author: this.editForm.get(['author'])!.value,
      description: this.editForm.get(['description'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? moment(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      closedAt: this.editForm.get(['closedAt'])!.value ? moment(this.editForm.get(['closedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      closedBy: this.editForm.get(['closedBy'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIssue>>): void {
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
