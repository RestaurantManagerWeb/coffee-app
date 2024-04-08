import { Button, NumberInput, NumberInputHandlers } from '@mantine/core';
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
    <Button.Group>
      <Button onClick={() => handlersRef.current?.decrement()}>-</Button>
      <NumberInput
        handlersRef={handlersRef}
        defaultValue={0}
        min={0}
        hideControls
        radius={0}
        onChange={onChange}
        value={value}
      />
      <Button onClick={() => handlersRef.current?.increment()}>+</Button>
    </Button.Group>
  );
}

export default QuantityInput;
