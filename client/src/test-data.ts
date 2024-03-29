import { MenuItem } from './types';

const randomPrice = () => Math.floor(Math.random() * 500);

export const menuItems: MenuItem[] = [
  { id: 1, name: 'Test Item 1', price: randomPrice() },
  { id: 2, name: 'Test Item 2', price: randomPrice() },
  { id: 3, name: 'Test Item 3', price: randomPrice() },
  { id: 4, name: 'Test Item 4', price: randomPrice() },
  { id: 5, name: 'Test Item 5', price: randomPrice() },
];
