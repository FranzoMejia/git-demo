export interface ICatalogo {
  id?: number;
  productoDesc?: string | null;
  precioSugerido?: number | null;
}

export class Catalogo implements ICatalogo {
  constructor(public id?: number, public productoDesc?: string | null, public precioSugerido?: number | null) {}
}

export function getCatalogoIdentifier(catalogo: ICatalogo): number | undefined {
  return catalogo.id;
}
