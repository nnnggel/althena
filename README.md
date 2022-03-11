# Althena - the first dex aggregator with smart order routing.

# Description
Althena is a solution that use a path optimization algorithm to aggregate multiple decentralized exchanges, combined with Algorand TEAL contract technology, to optimize users' trading behavior and results. 

This solution will maximize users' returns in decentralized exchange transactions. Among them, the path optimization algorithm, after obtaining the information such as the price and depth of the currency pair exchanged by the current user in multiple decentralized exchanges, pass the nodes along the depth of the system, and search as deep as possible. Balance between computational efficiency and price optimization, and finally split the exchange transaction into optimized N transactions to form a new transaction group. The split transactions will be in a group, guaranteeing the integrity of the transaction, i.e. either all succeed or all fail (rollback). We also use the TEAL contract to implement a threshold system to ensure that transactions are performed as expected, and to ensure the consistency of user exchange transaction behavior and results. 

The initial version of Althena will make the UI for direct use by end users. In the future, an SDK will be produced to empower financial derivatives as the infrastructure of the Algorand ecosystem. for Further, with the upgrade of TEAL contract technology, Althena has the opportunity to become the oracle of the Algorand ecosystem.

# Contact us
[website](https://www.althena.io)
[twitter](https://twitter.com/Althenalabs)
