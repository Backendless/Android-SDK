# Scheduler for timers

Some times timers became broken, to restore it create cron job

- create file `/opt/backendless/shared/confs/bl-timer/timer-start.sh` which you can download here [timer-start.sh](./timer-start.sh). 
The File should be available on each worker node.
- import yml [bl-timer](yml/bl-timer.yml) 