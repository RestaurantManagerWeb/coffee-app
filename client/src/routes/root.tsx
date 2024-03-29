import '@mantine/core/styles.css';
import { AppShell, Burger, NavLink } from '@mantine/core';
import { Link, Outlet, useLocation } from 'react-router-dom';
import { useDisclosure } from '@mantine/hooks';

function Root() {
  const [opened, { toggle }] = useDisclosure();
  const location = useLocation();

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
          active={location.pathname === '/menu'}
        />
        <NavLink
          to="/orders"
          component={Link}
          label="Orders"
          active={location.pathname === '/orders'}
        />
      </AppShell.Navbar>
      <AppShell.Main>
        <Outlet />
      </AppShell.Main>
    </AppShell>
  );
}

export default Root;
