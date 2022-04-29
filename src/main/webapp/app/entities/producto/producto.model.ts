import { IPedido } from 'app/entities/pedido/pedido.model';

export interface IProducto {
  id?: number;
  producto?: string;
  precio?: number | null;
  pedido?: IPedido | null;
}

export class Producto implements IProducto {
  constructor(public id?: number, public producto?: string, public precio?: number | null, public pedido?: IPedido | null) {}
}

export function getProductoIdentifier(producto: IProducto): number | undefined {
  return producto.id;
}
