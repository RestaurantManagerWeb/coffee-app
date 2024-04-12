import { Anchor, Card, Grid, SimpleGrid } from '@mantine/core';
import { Link, createFileRoute } from '@tanstack/react-router';
import { MenuGroup } from '../../types';
import { useEffect, useState } from 'react';
import Order from '../../Order';

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
    <SimpleGrid cols={2}>
      <Grid>
        {groups.map((group) => (
          <Grid.Col key={group.id} span={4}>
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
      <Order />
    </SimpleGrid>
  );
}
