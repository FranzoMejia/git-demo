import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CatalogoService } from '../service/catalogo.service';
import { ICatalogo, Catalogo } from '../catalogo.model';

import { CatalogoUpdateComponent } from './catalogo-update.component';

describe('Catalogo Management Update Component', () => {
  let comp: CatalogoUpdateComponent;
  let fixture: ComponentFixture<CatalogoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let catalogoService: CatalogoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CatalogoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CatalogoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CatalogoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    catalogoService = TestBed.inject(CatalogoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const catalogo: ICatalogo = { id: 456 };

      activatedRoute.data = of({ catalogo });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(catalogo));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Catalogo>>();
      const catalogo = { id: 123 };
      jest.spyOn(catalogoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: catalogo }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(catalogoService.update).toHaveBeenCalledWith(catalogo);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Catalogo>>();
      const catalogo = new Catalogo();
      jest.spyOn(catalogoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: catalogo }));
      saveSubject.complete();

      // THEN
      expect(catalogoService.create).toHaveBeenCalledWith(catalogo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Catalogo>>();
      const catalogo = { id: 123 };
      jest.spyOn(catalogoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(catalogoService.update).toHaveBeenCalledWith(catalogo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
