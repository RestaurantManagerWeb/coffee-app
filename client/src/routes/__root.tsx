import '@mantine/core/styles.css';
import { AppShell, Burger, NavLink } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { Link, Outlet, createRootRoute } from '@tanstack/react-router';

export const Route = createRootRoute({
  component: Root,
  notFoundComponent: () => <div></div>,
});

function Root() {
  const [opened, { toggle }] = useDisclosure();

  return (
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
          to="/orders"
          component={Link}
          label="Orders"
          active={location.pathname.startsWith('/orders')}
        />
      </AppShell.Navbar>
      <AppShell.Main>
        <Outlet />
      </AppShell.Main>
    </AppShell>
  );
}
