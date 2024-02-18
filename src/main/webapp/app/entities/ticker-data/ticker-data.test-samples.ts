import { ITickerData, NewTickerData } from './ticker-data.model';

export const sampleWithRequiredData: ITickerData = {
  id: 27808,
  date: 'zowie android next',
};

export const sampleWithPartialData: ITickerData = {
  id: 12310,
  date: 'anenst noisily per',
  adjClose: 15205.13,
  high: 7826.12,
  volume: 9505,
};

export const sampleWithFullData: ITickerData = {
  id: 21967,
  date: 'hammock above',
  adjClose: 17077.57,
  close: 16348.92,
  high: 32074.51,
  low: 29476.84,
  open: 5482.05,
  volume: 27,
};

export const sampleWithNewData: NewTickerData = {
  date: 'before',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
