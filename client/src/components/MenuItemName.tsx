import { useEffect, useState } from 'react';
import { MenuItem } from '../types';

async function getMenuItemName(menuItemId: number) {
  const res = await fetch(`/api/outlet/menu/item/${menuItemId}`);
  if (!res.ok) throw new Error(`Error fetching menu item`);
  const data = await res.json();
  console.log(data);
  return data as MenuItem;
}

function MenuItemName({ id }: Readonly<{ id: number }>) {
  const [name, setName] = useState('');

  useEffect(() => {
    (async () => setName((await getMenuItemName(id)).name))();
  }, [id]);

  return <>{name}</>;
}

export default MenuItemName;
