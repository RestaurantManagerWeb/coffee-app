import { createFileRoute } from '@tanstack/react-router';
import { MenuItem } from '../../../types';
import { Table, TableData } from '@mantine/core';

async function fetchMenuGroup(groupId: string): Promise<MenuItem[]> {
  const res = await fetch(`/api/outlet/menu/group/${groupId}`);
  if (!res.ok) throw new Error(res.statusText);
  return await res.json();
}

export const Route = createFileRoute('/menu/group/$groupId')({
  component: Group,
  beforeLoad: () => {
    return { title: `Group` };
  },
  loader: async ({ params }) => {
    return fetchMenuGroup(params.groupId);
  },
});

function Group() {
  const menuItems = Route.useLoaderData();

  const tableData: TableData = {
    head: ['ID', 'Name', 'Price'],
    body: menuItems.map((item) => [item.id, item.name, item.price]),
  };

  return (
    <Table
      striped
      highlightOnHover
      stickyHeader
      withTableBorder
      withColumnBorders
      data={tableData}
    />
  );
}
