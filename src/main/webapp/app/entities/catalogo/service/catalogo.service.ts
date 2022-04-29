import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICatalogo, getCatalogoIdentifier } from '../catalogo.model';

export type EntityResponseType = HttpResponse<ICatalogo>;
export type EntityArrayResponseType = HttpResponse<ICatalogo[]>;

@Injectable({ providedIn: 'root' })
export class CatalogoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/catalogos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(catalogo: ICatalogo): Observable<EntityResponseType> {
    return this.http.post<ICatalogo>(this.resourceUrl, catalogo, { observe: 'response' });
  }

  update(catalogo: ICatalogo): Observable<EntityResponseType> {
    return this.http.put<ICatalogo>(`${this.resourceUrl}/${getCatalogoIdentifier(catalogo) as number}`, catalogo, { observe: 'response' });
  }

  partialUpdate(catalogo: ICatalogo): Observable<EntityResponseType> {
    return this.http.patch<ICatalogo>(`${this.resourceUrl}/${getCatalogoIdentifier(catalogo) as number}`, catalogo, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICatalogo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICatalogo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCatalogoToCollectionIfMissing(catalogoCollection: ICatalogo[], ...catalogosToCheck: (ICatalogo | null | undefined)[]): ICatalogo[] {
    const catalogos: ICatalogo[] = catalogosToCheck.filter(isPresent);
    if (catalogos.length > 0) {
      const catalogoCollectionIdentifiers = catalogoCollection.map(catalogoItem => getCatalogoIdentifier(catalogoItem)!);
      const catalogosToAdd = catalogos.filter(catalogoItem => {
        const catalogoIdentifier = getCatalogoIdentifier(catalogoItem);
        if (catalogoIdentifier == null || catalogoCollectionIdentifiers.includes(catalogoIdentifier)) {
          return false;
        }
        catalogoCollectionIdentifiers.push(catalogoIdentifier);
        return true;
      });
      return [...catalogosToAdd, ...catalogoCollection];
    }
    return catalogoCollection;
  }
}
