import { IUser } from 'app/entities/user/user.model';

export interface ITicker {
  id: number;
  currency?: string | null;
  description?: string | null;
  displaySymbol?: string | null;
  figi?: string | null;
  isin?: string | null;
  mic?: string | null;
  shareClassFIGI?: string | null;
  symbol?: string | null;
  symbol2?: string | null;
  type?: string | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewTicker = Omit<ITicker, 'id'> & { id: null };
