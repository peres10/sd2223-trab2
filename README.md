# Social Media platform similar to Mastodon
Distributed Systems course project phase 2

The backend of a social media platform similar  to Mastodon, utilizing REST and SOAP protocols for 
seamless interoperability. Implemented robust concurrency control mechanisms to ensure efficient communication 
even in the event of failures.
In this phase the platform is integrated with the original Mastodon API, establishing a secure system with TLS
encryption and enabling data replication using Kafka.


This project implements both message propagation models,
controlled by the `-push true|false` flag as an extra argument of the Feeds server in [feeds.props](https://github.com/smduarte/sd2223-trab1/blob/main/feeds.props)

+ ***Push Version***
  
  Messages are forwarded to remote domains, as needed, when posted.
  
  Requires keeping for each user a list of "followees".
  
+ ***Pull Version***
  
  User feeds are materialized on demand, by pulling the messages from *followed* local and remote domain users, as needed.
  
  A cache is used to keep remote feeds for a short period of time, to improve performance.

The specific parts of each solution are implemented in classes that include *Push* or *Pull* in the name.
For SOAP, the remote API is split across two packages with *push* and "pull" in their names. This should
allow to create a project version with either just the *Pull* or "Push" version, with minimal changes.
