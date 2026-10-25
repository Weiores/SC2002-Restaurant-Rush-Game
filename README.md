# Restaurant Rush

## Stage 2 Customer Behaviour

| Customer | Satisfaction loss per waiting turn | Bill |
|---|---:|---|
| Regular | 8 | Full price |
| VIP | 5 | 10% discount |
| Critic | 12 | Full price |

Satisfaction decreases while customers are waiting or seated
and have not been served.

Critics choose the highest-priced menu item. If prices tie,
they choose the first item in menu order.

Regular and VIP customers choose food using seeded randomness.
The current game uses seed 42.

Customers arrive on turns 1, 4, 7, 10, 13 and 16.
The turn-4 arrival is a VIP, and the turn-10 arrival is a Critic.

All customer types follow the same ordering, serving and payment flow.
Their different behaviours are handled through customer methods.

## Gameplay and Test Runs

See the [Stage 1 Gameplay and Test Guide](Restaurant_Rush_Blueprint.md).