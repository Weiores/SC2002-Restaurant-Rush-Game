# Stage 1 Gameplay and Test Guide

## Restaurant Rush: Victory and Defeat Guide

This guide is based on the two sample game logs provided. The successful log uses **Game seed 42** and ends in victory after 18 turns. The defeat log also uses 18 turns, but the Waiter waits every turn and no dishes are served.

## What the two runs show

| Run | Paid customers | Unhappy departures | Revenue | Result |
|---|---:|---:|---:|---|
| Successful sample | 5 | 0 | $63.00 | Victory |
| Waited every turn | 0 | 2 | $0.00 | Defeat |

The successful run also served 5 dishes and ended with average paid-customer satisfaction of 74.40/100. It had a Pasta order in progress at the end, so the sixth customer did not need to be served for this particular run to show Victory.

## Main strategy

Each turn gives you separate choices for the Waiter and Chef:

1. **Seat customers promptly.** A customer left waiting loses satisfaction, and customers can leave unhappy if ignored.
2. **Take an order as soon as a seated customer is waiting to order.**
3. **Use the Chef to prepare the current order.** Salad and Burger take one preparation unit; Pasta takes two.
4. **Serve ready food on a later turn.** In the sample run, a dish could not be served on the same turn it became ready.
5. **Keep tables moving.** Customers automatically finish eating after a full turn, then pay automatically at the end of a later turn. Seat the next waiting customer when a table becomes available.
6. **Do not choose Wait when a useful task is available.** The defeat example shows that waiting repeatedly allows the queue to grow and customers to leave without spending.

The Chef can prepare an order during the same turn that the Waiter takes it. This is useful because it advances the meal without spending an extra turn.

## Reproducible successful run: seed 42

Follow this sequence from the successful sample log. Choose the matching task by its description in the game; menu option numbers can change as the available tasks change.

| Turn | Waiter action | Chef action / end-of-turn event |
|---:|---|---|
| 1 | Seat Customer 1 at Table 1 | No order to prepare yet. |
| 2 | Take Customer 1's order: Pasta | Cook Pasta for one unit (1/2). |
| 3 | Wait (no useful Waiter task is available in the sample) | Cook Pasta for the second unit; it becomes ready. |
| 4 | Serve Customer 1's Pasta | Customer 1 begins eating. |
| 5 | Seat Customer 2 at Table 2 | Customer 1 finishes eating and becomes ready to pay. |
| 6 | Take Customer 2's order: Salad | Cook Salad; it becomes ready. Customer 1 pays $18. |
| 7 | Serve Customer 2's Salad | Customer 2 begins eating. |
| 8 | Seat Customer 3 at Table 1 | Customer 2 finishes eating and becomes ready to pay. |
| 9 | Take Customer 3's order: Salad | Cook Salad; it becomes ready. Customer 2 pays $9. |
| 10 | Serve Customer 3's Salad | Customer 3 begins eating. |
| 11 | Seat Customer 4 at Table 2 | Customer 3 finishes eating and becomes ready to pay. |
| 12 | Take Customer 4's order: Pasta | Cook Pasta for one unit (1/2). Customer 3 pays $9. |
| 13 | Seat Customer 5 at Table 1 | Cook Pasta for the second unit; it becomes ready. |
| 14 | Serve Customer 4's Pasta | Customer 4 begins eating. |
| 15 | Take Customer 5's order: Salad | Cook Salad; it becomes ready. Customer 4 finishes eating. |
| 16 | Serve Customer 5's Salad | Customer 4 pays $18. |
| 17 | Seat Customer 6 at Table 2 | Customer 5 finishes eating and becomes ready to pay. |
| 18 | Take Customer 6's order: Burger | Cook Burger; it becomes ready. Customer 5 pays $9. |

### End-of-run result from the sample

- Served: **5**
- Paid: **5**
- Unhappy departures: **0**
- Revenue: **$63.00**
- Average satisfaction of paid customers: **74.40/100**
- Game result: **Victory**

## How the defeat happened

In the defeat log, the Waiter chose **Wait** for all 18 turns. No customers were seated, no orders were taken, and the Chef had nothing to prepare. Customers accumulated in the waiting queue and two left unhappy. The run ended with:

- Served: **0**
- Paid: **0**
- Unhappy departures: **2**
- Revenue: **$0.00**
- Game result: **Defeat**

---

# Stage 2 Gameplay and Test Guide

## Restaurant Rush: Victory and Defeat Guide (VIP and Critic customers)

This guide is based on the two Stage 2 sample game logs. Both use **Game seed 42** and 18 turns. The successful log ends in victory with 4 paid customers. The defeat log has the Waiter wait every turn and no dishes are served.

## What is new in Stage 2

Customers now come in three types:

| Type | Satisfaction lost per turn (waiting or seated before being served) | Observed price |
|---|---:|---|
| Regular | 8 | Salad $9.00, Pasta $18.00 |
| VIP | 5 | Salad $8.10 (a 10% lower price than a Regular's Salad) |
| Critic | 12 | Pasta $18.00 |

- A customer's satisfaction stops dropping once their dish is served. The paid-customer average uses the satisfaction at that point.
- A customer leaves unhappy when their satisfaction runs out while waiting in the queue (Regular at 4 and Critic at 4 left on the next turn).
- The Critic loses satisfaction fastest, so seat and serve them promptly. The VIP is the most patient but pays less for the same dish.

## What the two runs show

| Run | Served | Paid customers | Unhappy departures | Revenue | Result |
|---|---:|---:|---:|---:|---|
| Successful sample | 5 | 4 | 0 | $53.10 | Victory |
| Waited every turn | 0 | 0 | 2 | $0.00 | Defeat |

The successful run ended with average paid-customer satisfaction of 72.25/100. At the end, Customer 5 was ready to pay and Customer 6 had just been seated, so neither needed to finish for this run to show Victory.

## Arrival schedule (seed 42, Stage 2)

Arrivals are identical in both runs. A customer appears in the queue on the turn after the arrival message.

| Customer | Type | First appears in queue |
|---|---|---|
| 1 | Regular | Turn 1 |
| 2 | VIP | Turn 4 |
| 3 | Regular | Turn 7 |
| 4 | Critic | Turn 10 |
| 5 | Regular | Turn 13 |
| 6 | Regular | Turn 16 |

## Main strategy

1. **Serve ready food before seating the next customer.** In every turn where both were possible (turns 4, 7, 10, 14, 17), the successful run served the dish and seated the queued customer on the following turn.
2. **Seat a waiting customer as soon as a table is free and nothing is ready to serve.**
3. **Take an order as soon as a seated customer has none.** The Chef can start cooking in the same turn.
4. **Use the Chef every turn there is an order.** Salad takes one unit, Pasta takes two.
5. **A dish cannot be served on the turn it becomes ready.** Serve it on the next turn.
6. **Customers finish eating one turn after being served, then pay at the end of the following turn.** Tables free up automatically.
7. **Never Wait when another task is available.** The defeat run shows what happens.

## Reproducible successful run: seed 42, Stage 2

Choose the matching task by its description in the game. Menu option numbers change as the available tasks change.

| Turn | Waiter action | Chef action / end-of-turn event |
|---:|---|---|
| 1 | Seat Customer 1 (Regular) at Table 1 | Nothing to prepare. |
| 2 | Take Customer 1's order: Pasta ($18.00) | Cook Pasta for one unit (1/2). |
| 3 | Wait (the only task available) | Cook Pasta for the second unit; it becomes ready. Customer 2 (VIP) arrives. |
| 4 | Serve Pasta to Customer 1 (chosen over seating Customer 2) | Nothing to prepare. |
| 5 | Seat Customer 2 (VIP) at Table 2 | Nothing to prepare. Customer 1 finishes eating and is ready to pay. |
| 6 | Take Customer 2's order: Salad ($8.10) | Cook Salad; it becomes ready. Customer 1 pays $18.00. Customer 3 (Regular) arrives. |
| 7 | Serve Salad to Customer 2 (chosen over seating Customer 3) | Nothing to prepare. |
| 8 | Seat Customer 3 at Table 1 | Nothing to prepare. Customer 2 finishes eating and is ready to pay. |
| 9 | Take Customer 3's order: Salad ($9.00) | Cook Salad; it becomes ready. Customer 2 pays $8.10. Customer 4 (Critic) arrives. |
| 10 | Serve Salad to Customer 3 (chosen over seating Customer 4) | Nothing to prepare. |
| 11 | Seat Customer 4 (Critic) at Table 2 | Nothing to prepare. Customer 3 finishes eating and is ready to pay. |
| 12 | Take Customer 4's order: Pasta ($18.00) | Cook Pasta for one unit (1/2). Customer 3 pays $9.00. Customer 5 (Regular) arrives. |
| 13 | Seat Customer 5 at Table 1 | Cook Pasta for the second unit; it becomes ready. |
| 14 | Serve Pasta to Customer 4 (chosen over taking Customer 5's order) | Nothing to prepare. |
| 15 | Take Customer 5's order: Pasta ($18.00) | Cook Pasta for one unit (1/2). Customer 4 finishes eating and is ready to pay. Customer 6 (Regular) arrives. |
| 16 | Wait (the only task available) | Cook Pasta for the second unit; it becomes ready. Customer 4 pays $18.00. |
| 17 | Serve Pasta to Customer 5 (chosen over seating Customer 6) | Nothing to prepare. |
| 18 | Seat Customer 6 at Table 2 | Nothing to prepare. Customer 5 finishes eating and is ready to pay. |

### End-of-run result from the sample

- Served: **5**
- Paid: **4**
- Unhappy departures: **0**
- Revenue: **$53.10** ($18.00 + $8.10 + $9.00 + $18.00)
- Average satisfaction of paid customers: **72.25/100** (satisfaction at serving: 76, 85, 76, 52)
- Game result: **Victory**

## How the defeat happened

In the defeat log, the Waiter chose **Wait** on all 18 turns. Both tables stayed empty, no orders were taken, and the Chef never had anything to prepare.

| Turn | Waiter action | Event |
|---:|---|---|
| 1 | Wait | Customer 1 waiting (satisfaction 100). |
| 2 | Wait | |
| 3 | Wait | Customer 2 (VIP) arrives. |
| 4 | Wait | |
| 5 | Wait | |
| 6 | Wait | Customer 3 (Regular) arrives. |
| 7 | Wait | |
| 8 | Wait | |
| 9 | Wait | Customer 4 (Critic) arrives. |
| 10 | Wait | |
| 11 | Wait | |
| 12 | Wait | Customer 5 (Regular) arrives. |
| 13 | Wait | Customer 1 ran out of patience and left. |
| 14 | Wait | |
| 15 | Wait | Customer 6 (Regular) arrives. |
| 16 | Wait | |
| 17 | Wait | |
| 18 | Wait | Customer 4 (Critic) ran out of patience and left. |

The run ended with:

- Served: **0**
- Paid: **0**
- Unhappy departures: **2**
- Revenue: **$0.00**
- Game result: **Defeat**
