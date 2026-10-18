# Restaurant Rush: Victory and Defeat Guide

This guide is based on the two sample game logs provided. The successful log uses **Game seed 42** and ends in victory after 18 turns. The defeat log also uses 18 turns, but the Waiter waits every turn and no dishes are served.

> The exact victory and defeat rules are not stated in the logs. The numbers below are the results of the sample runs, not a confirmed minimum win requirement.

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

## Quick checklist during play

- Is anyone waiting in the queue? Seat a customer if a table is free.
- Is anyone seated without an order? Take that customer's order.
- Is there an order to prepare? Use the Chef rather than letting preparation wait.
- Is a dish ready? Serve it when the Waiter can.
- Is a customer ready to pay? Payment happens automatically after the required eating turn; then use the freed table for another customer.
- Are customers waiting too long? Prioritize them before they lose too much satisfaction or leave.

