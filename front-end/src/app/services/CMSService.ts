import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, map, Observable, of, shareReplay } from "rxjs";
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CMSService {

  private readonly baseUrl = environment.apiUrl;
  private cache = new Map<string, Observable<any>>();

  constructor(private http: HttpClient) { }

  getSingleType<T = any>(endpoint: string): Observable<T | null> {
    const cacheKey = `single-${endpoint}`;
    
    if (!this.cache.has(cacheKey)) {
      const request$ = this.http
        .get<T>(`${this.baseUrl}/content/${endpoint}?populate=*`)
        .pipe(
          map(res => res ?? null),
          catchError(() => of(null)),
          shareReplay(1)
        );
      this.cache.set(cacheKey, request$);
    }
    
    return this.cache.get(cacheKey)!;
  }

  getCollection<T = any>(endpoint: string): Observable<T[]> {
    const cacheKey = `collection-${endpoint}`;

    if (!this.cache.has(cacheKey)) {
      const request$ = this.http
        .get<T[]>(`${this.baseUrl}/content/${endpoint}?populate=*`)
        .pipe(
          map(res => Array.isArray(res) ? res : []),
          catchError(() => of([])),
          shareReplay(1)
        );
      this.cache.set(cacheKey, request$);
    }

    return this.cache.get(cacheKey)!;
  }

}