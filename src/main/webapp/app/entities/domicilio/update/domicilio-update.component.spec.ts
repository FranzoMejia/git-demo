import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DomicilioService } from '../service/domicilio.service';
import { IDomicilio, Domicilio } from '../domicilio.model';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';

import { DomicilioUpdateComponent } from './domicilio-update.component';

describe('Domicilio Management Update Component', () => {
  let comp: DomicilioUpdateComponent;
  let fixture: ComponentFixture<DomicilioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let domicilioService: DomicilioService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DomicilioUpdateComponent],
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
      .overrideTemplate(DomicilioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DomicilioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    domicilioService = TestBed.inject(DomicilioService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cliente query and add missing value', () => {
      const domicilio: IDomicilio = { id: 456 };
      const cliente: ICliente = { id: 63118 };
      domicilio.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 86147 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ domicilio });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(clienteCollection, ...additionalClientes);
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const domicilio: IDomicilio = { id: 456 };
      const cliente: ICliente = { id: 35601 };
      domicilio.cliente = cliente;

      activatedRoute.data = of({ domicilio });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(domicilio));
      expect(comp.clientesSharedCollection).toContain(cliente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Domicilio>>();
      const domicilio = { id: 123 };
      jest.spyOn(domicilioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domicilio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: domicilio }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(domicilioService.update).toHaveBeenCalledWith(domicilio);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Domicilio>>();
      const domicilio = new Domicilio();
      jest.spyOn(domicilioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domicilio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: domicilio }));
      saveSubject.complete();

      // THEN
      expect(domicilioService.create).toHaveBeenCalledWith(domicilio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Domicilio>>();
      const domicilio = { id: 123 };
      jest.spyOn(domicilioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domicilio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(domicilioService.update).toHaveBeenCalledWith(domicilio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackClienteById', () => {
      it('Should return tracked Cliente primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClienteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
