export interface StrapiEntity<T> {
  id: number;
  attributes: T;
}

export interface StrapiSingleResponse<T> {

  data: StrapiEntity<T> | null;

  meta?: unknown;

}

export interface StrapiCollectionResponse<T> {

  data: StrapiEntity<T>[];

  meta?: unknown;

}