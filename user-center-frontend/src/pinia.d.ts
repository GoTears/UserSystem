import "pinia";

declare module "pinia" {
  export interface DefineStoreOptionsBase<_S, _Store> {
    persist?:
      | {
          enabled?: boolean;
          strategies?: Array<{
            key?: string;
            storage?: Storage;
            paths?: string[];
          }>;
        }
      | Array<{
          key?: string;
          storage?: Storage;
          paths?: string[];
        }>;
  }

  export interface DefineSetupStoreOptions<_Id, _S, _G, _A> {
    persist?:
      | {
          enabled?: boolean;
          strategies?: Array<{
            key?: string;
            storage?: Storage;
            paths?: string[];
          }>;
        }
      | Array<{
          key?: string;
          storage?: Storage;
          paths?: string[];
        }>;
  }
}
