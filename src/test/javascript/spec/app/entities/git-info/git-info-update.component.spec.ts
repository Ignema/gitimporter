import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GitimporterTestModule } from '../../../test.module';
import { GitInfoUpdateComponent } from 'app/entities/git-info/git-info-update.component';
import { GitInfoService } from 'app/entities/git-info/git-info.service';
import { GitInfo } from 'app/shared/model/git-info.model';

describe('Component Tests', () => {
  describe('GitInfo Management Update Component', () => {
    let comp: GitInfoUpdateComponent;
    let fixture: ComponentFixture<GitInfoUpdateComponent>;
    let service: GitInfoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GitimporterTestModule],
        declarations: [GitInfoUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(GitInfoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GitInfoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GitInfoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new GitInfo(123);
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
        const entity = new GitInfo();
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
