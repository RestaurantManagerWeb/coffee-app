import {
  ActionIcon,
  Group,
  NumberInput,
  NumberInputHandlers,
} from '@mantine/core';
import { IconMinus, IconPlus } from '@tabler/icons-react';
import { useRef } from 'react';

function QuantityInput({
  onChange,
  value,
}: Readonly<{
  onChange?: (value: number | string) => void;
  value?: string | number;
}>) {
  const handlersRef = useRef<NumberInputHandlers>(null);

  return (
    <Group>
      <ActionIcon onClick={() => handlersRef.current?.decrement()}>
        <IconMinus />
      </ActionIcon>
      <NumberInput
        handlersRef={handlersRef}
        defaultValue={0}
        min={0}
        hideControls
        onChange={onChange}
        value={value}
        flex={1}
      />
      <ActionIcon onClick={() => handlersRef.current?.increment()}>
        <IconPlus />
      </ActionIcon>
    </Group>
  );
}

export default QuantityInput;
