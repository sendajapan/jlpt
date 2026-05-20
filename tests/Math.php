<?php

namespace Tests;

class Math
{

    private static int $value;

    public function __construct()
    {
        $this->value = 10;
    }


    public static function start(): self
    {
        return new Math();
    }

    public function addTen(): self
    {
        $this->value += 10;
        return $this;
    }

    public function subtractTow(): self{
        $this->value -= 2;
        return $this;
    }

    public function getValue()
    {
        echo $this->value;
    }

}

Math::start()::addTen()->subtractTow()->getValue();

//$math = new Math();
//$math->start();
//$math->addTen();
//$math->subtractTow();
//$math->getValue();

