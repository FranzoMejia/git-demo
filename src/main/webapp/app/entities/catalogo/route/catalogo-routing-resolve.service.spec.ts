import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICatalogo, Catalogo } from '../catalogo.model';
import { CatalogoService } from '../service/catalogo.service';

import { CatalogoRoutingResolveService } from './catalogo-routing-resolve.service';

describe('Catalogo routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CatalogoRoutingResolveService;
  let service: CatalogoService;
  let resultCatalogo: ICatalogo | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(CatalogoRoutingResolveService);
    service = TestBed.inject(CatalogoService);
    resultCatalogo = undefined;
  });

  describe('resolve', () => {
    it('should return ICatalogo returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCatalogo = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCatalogo).toEqual({ id: 123 });
    });

    it('should return new ICatalogo if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCatalogo = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCatalogo).toEqual(new Catalogo());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Catalogo })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCatalogo = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCatalogo).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
