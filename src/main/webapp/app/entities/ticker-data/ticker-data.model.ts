import { ITicker } from 'app/entities/ticker/ticker.model';

export interface ITickerData {
  id?: number;
  date?: string | null;
  adjClose?: number | null;
  close?: number | null;
  high?: number | null;
  low?: number | null;
  open?: number | null;
  volume?: number | null;
  ticker?: Pick<ITicker, 'id' | 'symbol'> | null;
}

export type NewTickerData = Omit<ITickerData, 'id'> & { id: null };
