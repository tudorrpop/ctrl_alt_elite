import { Injectable } from '@angular/core';
import { Storage } from '@ionic/storage-angular';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  private _storage: Storage | null = null;
  private readonly ready: Promise<void>;

  constructor(private storage: Storage) {
    this.ready = this.init();
  }

  private async init() {
    this._storage = await this.storage.create();
  }

  private async ensureReady(): Promise<void> {
    await this.ready;
  }

  public async set<T>(key: string, value: T): Promise<void> {
    await this.ensureReady();
    await this._storage?.set(key, value);
  }

  public async get<T = any>(key: string): Promise<T | null> {
    await this.ensureReady();
    return this._storage?.get(key) ?? null;
  }

  public async remove(key: string): Promise<void> {
    await this.ensureReady();
    await this._storage?.remove(key);
  }

  public async clear(): Promise<void> {
    await this.ensureReady();
    await this._storage?.clear();
  }
}
