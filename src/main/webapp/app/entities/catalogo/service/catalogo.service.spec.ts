import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICatalogo, Catalogo } from '../catalogo.model';

import { CatalogoService } from './catalogo.service';

describe('Catalogo Service', () => {
  let service: CatalogoService;
  let httpMock: HttpTestingController;
  let elemDefault: ICatalogo;
  let expectedResult: ICatalogo | ICatalogo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CatalogoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      productoDesc: 'AAAAAAA',
      precioSugerido: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Catalogo', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Catalogo()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Catalogo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          productoDesc: 'BBBBBB',
          precioSugerido: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Catalogo', () => {
      const patchObject = Object.assign(
        {
          productoDesc: 'BBBBBB',
          precioSugerido: 1,
        },
        new Catalogo()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Catalogo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          productoDesc: 'BBBBBB',
          precioSugerido: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Catalogo', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCatalogoToCollectionIfMissing', () => {
      it('should add a Catalogo to an empty array', () => {
        const catalogo: ICatalogo = { id: 123 };
        expectedResult = service.addCatalogoToCollectionIfMissing([], catalogo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(catalogo);
      });

      it('should not add a Catalogo to an array that contains it', () => {
        const catalogo: ICatalogo = { id: 123 };
        const catalogoCollection: ICatalogo[] = [
          {
            ...catalogo,
          },
          { id: 456 },
        ];
        expectedResult = service.addCatalogoToCollectionIfMissing(catalogoCollection, catalogo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Catalogo to an array that doesn't contain it", () => {
        const catalogo: ICatalogo = { id: 123 };
        const catalogoCollection: ICatalogo[] = [{ id: 456 }];
        expectedResult = service.addCatalogoToCollectionIfMissing(catalogoCollection, catalogo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalogo);
      });

      it('should add only unique Catalogo to an array', () => {
        const catalogoArray: ICatalogo[] = [{ id: 123 }, { id: 456 }, { id: 30641 }];
        const catalogoCollection: ICatalogo[] = [{ id: 123 }];
        expectedResult = service.addCatalogoToCollectionIfMissing(catalogoCollection, ...catalogoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const catalogo: ICatalogo = { id: 123 };
        const catalogo2: ICatalogo = { id: 456 };
        expectedResult = service.addCatalogoToCollectionIfMissing([], catalogo, catalogo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalogo);
        expect(expectedResult).toContain(catalogo2);
      });

      it('should accept null and undefined values', () => {
        const catalogo: ICatalogo = { id: 123 };
        expectedResult = service.addCatalogoToCollectionIfMissing([], null, catalogo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(catalogo);
      });

      it('should return initial array if no Catalogo is added', () => {
        const catalogoCollection: ICatalogo[] = [{ id: 123 }];
        expectedResult = service.addCatalogoToCollectionIfMissing(catalogoCollection, undefined, null);
        expect(expectedResult).toEqual(catalogoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
