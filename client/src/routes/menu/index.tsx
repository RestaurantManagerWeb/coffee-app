import { Anchor, Card, Grid } from '@mantine/core';
import { Link, createFileRoute } from '@tanstack/react-router';
import { MenuGroup } from '../../types';
import { useEffect, useState } from 'react';

export const Route = createFileRoute('/menu/')({
  component: Menu,
  loader: ({ context }) => ({
    context,
  }),
});

function Menu() {
  const [groups, setGroups] = useState<MenuGroup[]>([]);

  useEffect(() => {
    (async () => {
      const res = await fetch('/api/outlet/menu');

      if (!res.ok) return;
      const data = await res.json();

      setGroups(data);
    })();
  }, []);

  return (
    <Grid>
      {groups.map((group) => (
        <Grid.Col key={group.id} span={3}>
          <Anchor
            component={Link}
            to="/menu/group/$groupId"
            params={{ groupId: group.id.toString() }}
          >
            <Card shadow="sm" padding="lg" radius="md" withBorder>
              {group.name}
            </Card>
          </Anchor>
        </Grid.Col>
      ))}
    </Grid>
  );
}
