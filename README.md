# Lethal Minecart

## Description

A bukkit plugin that damage any creature that is hit by a running minecart (and launch them to the air!). 

The damage deals to the creatures is determined by the speed of the minecart (block per tick), the faster the minecart, the more damage it may deal, but is reducible by the armor of the creature.

The list of available commands:

- /lm max_damage <double>
  - Set the maximum damage a running minecart can deal to the creature (at or higher than the fault speed)
- /lm fatal_speed <double>
  - Set the speed that would deal the maximum damage to the creature
- /lm harmful_speed <double>
  - Set the minimum speed that would start to deal damage to the creature
- /lmvar
  - Check the configured values

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Installation

Just put the **jar** file to the plugin folder of the server. It will create a configuration folder in the plugin folder the first time it run.

## Usage

The formula the plugin use is really simple:

``Round((speed - minumum speed) / fatal speed * maximum damage)``

Please note that the unit of speed is **block per tick**. However, since I can't figure out how to get the tps the server is curent running at, the plugin would assume that the server runs at 20 tps. 

It may cause some problem if the server is experiencing hard time or the op has set the tps of the server to a different value.

## Contributing

Oh you wanna contribute on this bukkit plugin? Just submit a push request or make any commet.

## License

[LICENSE](LICENSE)

## Acknowledgements

Thanks TwoCool4Yo for giving me this plugin idea. Go check out his Youtube channel, there is a bunch of funny video!

## Contact
