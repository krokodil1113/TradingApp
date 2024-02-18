import { ITicker, NewTicker } from './ticker.model';

export const sampleWithRequiredData: ITicker = {
  id: 20009,
  symbol: 'divider fresh meaningfully',
};

export const sampleWithPartialData: ITicker = {
  id: 18788,
  description: 'exhausted experimentation greedily',
  displaySymbol: 'save and',
  isin: 'loudly',
  mic: 'sternly',
  symbol: 'provided',
  symbol2: 'thin',
  type: 'nearly than stampede',
};

export const sampleWithFullData: ITicker = {
  id: 21208,
  currency: 'testing',
  description: 'out',
  displaySymbol: 'towards real',
  figi: 'correctly',
  isin: 'creation',
  mic: 'swine yahoo',
  shareClassFIGI: 'curtain frightfully',
  symbol: 'aw',
  symbol2: 'for',
  type: 'speedily',
};

export const sampleWithNewData: NewTicker = {
  symbol: 'which',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
