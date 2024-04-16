import '@mantine/core/styles.css';
import { AppShell, Burger, NavLink } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { Link, Outlet, createRootRoute } from '@tanstack/react-router';
import { createContext, useMemo, useState } from 'react';

export const Route = createRootRoute({
  component: Root,
  notFoundComponent: () => <div></div>,
});

export const OrderContext = createContext<{
  items: { [id: number]: number };
  setOrderQuantity: (id: number, quantity: number) => void;
} | null>(null);

function Root() {
  const [opened, { toggle }] = useDisclosure();
  const [order, setOrder] = useState<{ [id: number]: number }>([]);

  const orderData = useMemo(
    () => ({
      items: order,
      setOrderQuantity: (id: number, quantity: number) => {
        setOrder((prev) => {
          const prevOrder = { ...prev, [id]: quantity };
          return prevOrder;
        });
      },
    }),
    [order]
  );

  return (
    <OrderContext.Provider value={orderData}>
      <AppShell
        header={{ height: 60 }}
        navbar={{
          width: '300',
          breakpoint: 'sm',
          collapsed: { mobile: !opened },
        }}
        padding="md"
      >
        <AppShell.Header>
          <Burger opened={opened} onClick={toggle} hiddenFrom="sm" size="sm" />
        </AppShell.Header>
        <AppShell.Navbar p="md">
          <NavLink
            to="/menu"
            component={Link}
            label="Menu"
            active={location.pathname.startsWith('/menu')}
          />
          <NavLink
            to="/order"
            component={Link}
            label="Orders"
            active={location.pathname.startsWith('/order')}
          />
        </AppShell.Navbar>
        <AppShell.Main>
          <Outlet />
        </AppShell.Main>
      </AppShell>
    </OrderContext.Provider>
  );
}
