import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PedidoService } from '../service/pedido.service';
import { IPedido, Pedido } from '../pedido.model';
import { IDomicilio } from 'app/entities/domicilio/domicilio.model';
import { DomicilioService } from 'app/entities/domicilio/service/domicilio.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';

import { PedidoUpdateComponent } from './pedido-update.component';

describe('Pedido Management Update Component', () => {
  let comp: PedidoUpdateComponent;
  let fixture: ComponentFixture<PedidoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pedidoService: PedidoService;
  let domicilioService: DomicilioService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PedidoUpdateComponent],
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
      .overrideTemplate(PedidoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PedidoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pedidoService = TestBed.inject(PedidoService);
    domicilioService = TestBed.inject(DomicilioService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call domicilio query and add missing value', () => {
      const pedido: IPedido = { id: 456 };
      const domicilio: IDomicilio = { id: 78864 };
      pedido.domicilio = domicilio;

      const domicilioCollection: IDomicilio[] = [{ id: 42441 }];
      jest.spyOn(domicilioService, 'query').mockReturnValue(of(new HttpResponse({ body: domicilioCollection })));
      const expectedCollection: IDomicilio[] = [domicilio, ...domicilioCollection];
      jest.spyOn(domicilioService, 'addDomicilioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pedido });
      comp.ngOnInit();

      expect(domicilioService.query).toHaveBeenCalled();
      expect(domicilioService.addDomicilioToCollectionIfMissing).toHaveBeenCalledWith(domicilioCollection, domicilio);
      expect(comp.domiciliosCollection).toEqual(expectedCollection);
    });

    it('Should call Cliente query and add missing value', () => {
      const pedido: IPedido = { id: 456 };
      const cliente: ICliente = { id: 73250 };
      pedido.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 77011 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pedido });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(clienteCollection, ...additionalClientes);
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pedido: IPedido = { id: 456 };
      const domicilio: IDomicilio = { id: 84838 };
      pedido.domicilio = domicilio;
      const cliente: ICliente = { id: 1681 };
      pedido.cliente = cliente;

      activatedRoute.data = of({ pedido });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(pedido));
      expect(comp.domiciliosCollection).toContain(domicilio);
      expect(comp.clientesSharedCollection).toContain(cliente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pedido>>();
      const pedido = { id: 123 };
      jest.spyOn(pedidoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pedido });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pedido }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(pedidoService.update).toHaveBeenCalledWith(pedido);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pedido>>();
      const pedido = new Pedido();
      jest.spyOn(pedidoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pedido });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pedido }));
      saveSubject.complete();

      // THEN
      expect(pedidoService.create).toHaveBeenCalledWith(pedido);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pedido>>();
      const pedido = { id: 123 };
      jest.spyOn(pedidoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pedido });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pedidoService.update).toHaveBeenCalledWith(pedido);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDomicilioById', () => {
      it('Should return tracked Domicilio primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDomicilioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackClienteById', () => {
      it('Should return tracked Cliente primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClienteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
