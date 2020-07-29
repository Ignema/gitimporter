import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GitimporterTestModule } from '../../../test.module';
import { IssueUpdateComponent } from 'app/entities/issue/issue-update.component';
import { IssueService } from 'app/entities/issue/issue.service';
import { Issue } from 'app/shared/model/issue.model';

describe('Component Tests', () => {
  describe('Issue Management Update Component', () => {
    let comp: IssueUpdateComponent;
    let fixture: ComponentFixture<IssueUpdateComponent>;
    let service: IssueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GitimporterTestModule],
        declarations: [IssueUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(IssueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IssueUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IssueService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Issue(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Issue();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
