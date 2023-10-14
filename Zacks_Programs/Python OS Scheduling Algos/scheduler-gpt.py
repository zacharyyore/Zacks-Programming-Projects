#Assignment 1 COP 4600
#Zachary Yore
#Carlos Cardenas
#Caleb Gibson
#Randall Roberts

import sys
from queue import PriorityQueue

class Process:
    def __init__(self, name, arrival_time, execution_time):
        self.name = name
        self.arrival_time = arrival_time
        self.execution_time = execution_time
        self.remaining_time = execution_time  # track remaining execution time
        self.start_time = None  
        self.finish_time = None
        self.response_time = None

    def __lt__(self, other):
        # this is needed for the priority queue to compare processes based on their remaining_time
        return self.remaining_time < other.remaining_time

    def __str__(self):
        return f"{self.name} {self.arrival_time} {self.execution_time} {self.remaining_time} {self.start_time} {self.finish_time}"


# def read_input_file(filename):
#     with open(filename, 'r') as file:
#         lines = file.readlines()
    
#     process_count = int(lines[0].split()[1])
#     total_time = int(lines[1].split()[1])
#     scheduling_algo = lines[2].split()[1]
    
#     processes = []
    
#     time_slice = None
#     if scheduling_algo == 'rr':
#         time_slice = int(lines[3].split()[1])
#         start_index = 5
#     else:
#         start_index = 4

#     for i in range(start_index, start_index + process_count - 1):
#         parts = lines[i].strip().split()
#         print(parts)
#         name = parts[1] #TODO: Note Intervened (changed index)
#         arrival_time = int(parts[3])    #TODO: Note Intervened (changed index)
#         burst_time = int(parts[5])  #TODO: Note Intervened (changed index)
#         processes.append(Process(name, arrival_time, burst_time))
    
#     return process_count, total_time, scheduling_algo, processes, time_slice

def read_input_file(filename):
    with open(filename, 'r') as file:
        lines = file.readlines()

    process_count = 0
    total_time = 0
    scheduling_algo = ""
    time_slice = None
    processes = []

    read_time_slice = False  # Flag to indicate if we are reading the time slice

    for line in lines:
        line = line.strip()

        # Remove comments on the same line (everything after '#')
        if '#' in line:
            line = line.split('#')[0].strip()

        if not line:
            continue

        parts = line.split()
        keyword = parts[0]

        if keyword == "processcount":
            process_count = int(parts[1])
        elif keyword == "runfor":
            total_time = int(parts[1])
        elif keyword == "use":
            scheduling_algo = parts[1]
            read_time_slice = True  # Set the flag to indicate that time slice should be read next
        elif read_time_slice and keyword == "quantum":
            if scheduling_algo == "rr":
                time_slice = int(parts[1])
            else:
                raise ValueError("Error: 'quantum' is only a valid parameter for the Round Robin scheduling algorithm")
        elif keyword == "end":
            break
        else:
            name = parts[2]  # Process name is in parts[2]
            arrival_time = int(parts[4])
            burst_time = int(parts[6])
            processes.append(Process(name, arrival_time, burst_time))
            read_time_slice = False  # Reset the flag

    return process_count, total_time, scheduling_algo, processes, time_slice


def write_output_file(filename, log, processes, scheduling_algo, time_slice, total_time):
    with open(filename, 'w') as file:
        file.write(f"{len(processes)} processes\n")
        
        if scheduling_algo == "fcfs":
            file.write("Using First-Come-First-Serve (FCFS)\n")
        elif scheduling_algo == "sjf":
            file.write("Using preemptive Shortest Job First\n")
        elif scheduling_algo == "stcf":
            file.write("Using Shortest Time to Completion First\n")
        elif scheduling_algo == "rr":
            file.write(f"Using Round-Robin\nQuantum   {time_slice}\n\n")
        
        for entry in log:
            if "arrived" in entry or "selected" in entry or "finished" in entry:
                file.write(f"{entry}\n")
            else:
                file.write(entry + '\n')
        
        file.write(f"Finished at time {total_time:>3}\n\n")
        
        # Sort the processes by their name before generating the report
        processes.sort(key=lambda p: p.name)
        
        for process in processes:
            wait_time, turnaround_time, response_time = calculate_metrics(process)
            if wait_time is None or turnaround_time is None or response_time is None:
                file.write(f"{process.name} did not finish\n")
            else:
                file.write(f"{process.name} wait {wait_time:>3} turnaround {turnaround_time:>3} response {response_time:>3}\n")








# ... (keeping the read_input_file and write_output_file functions as they are)

def sjf_scheduling(processes, total_time):
    log = []
    processes_copy = sorted(processes, key=lambda p: p.arrival_time)
    current_time = 0
    process_queue = []
    current_process = None

    while current_time < total_time:
        # Handle arriving processes
        while processes_copy and processes_copy[0].arrival_time <= current_time:
            arriving_process = processes_copy.pop(0)
            log.append(f"Time {current_time:>3} : {arriving_process.name} arrived")
            process_queue.append(arriving_process)
            process_queue.sort(key=lambda p: p.remaining_time)

        # Preemption: check if there's a process in the queue with shorter remaining time than the current process
        if process_queue and (current_process is None or process_queue[0].remaining_time < current_process.remaining_time):
            if current_process:
                process_queue.append(current_process)  # re-queue the current process
            process_queue.sort(key=lambda p: p.remaining_time)  # sort by remaining time, SJF
            current_process = process_queue.pop(0)  # select the process with the shortest remaining time
            if current_process.start_time is None:
                current_process.start_time = current_time  # set start time if this is the first time the process is selected
                current_process.response_time = current_time - current_process.arrival_time  # set response time
            log.append(f"Time {current_time:>3} : {current_process.name} selected (burst {current_process.remaining_time:3})")

        if current_process:
            # Execute the current process
            current_process.remaining_time -= 1
            # If the current process is finished
            if current_process.remaining_time == 0:
                current_process.finish_time = current_time + 1  # Process finishes at the next time unit
                log.append(f"Time {current_time + 1:>3} : {current_process.name} finished")
                current_process = None  # No current process

        # Log idle time if necessary
        if not current_process and not process_queue and current_time < total_time:
            log.append(f"Time {current_time:>3} : Idle")

        # Increment time
        current_time += 1

    # Log any processes that didn't finish
    for process in processes:
        if process.finish_time is None:
            log.append(f"Process {process.name} did not finish")

    return log, None








def fifo_scheduling(processes, total_time):
    log = []
    execution_order = []

    processes.sort(key=lambda x: x.arrival_time)
    current_time = 0

    for process in processes:
        if process.arrival_time >= total_time or current_time >= total_time:
            break
        
        while process.arrival_time > current_time:
            log.append(f"Time {current_time:>3} : Idle")
            current_time += 1
        
        log.append(f"Time {current_time:>3} : {process.name} arrived")
        process.start_time = current_time
        log.append(f"Time {current_time:>3} : {process.name} selected (burst {process.execution_time})")
        
        execution_time_remaining = process.execution_time  
        while execution_time_remaining > 0 and current_time < total_time:
            current_time += 1
            execution_time_remaining -= 1
        
        if execution_time_remaining == 0:
            process.finish_time = current_time
            log.append(f"Time {current_time:>3} : {process.name} finished")
            execution_order.append(process)
        else:
            log.append(f"Time {current_time:>3} : {process.name} stopped (unfinished)")
            break
    
    while current_time < total_time:
        log.append(f"Time {current_time:>3} : Idle")
        current_time += 1

    for process in processes:
        if process.finish_time is None:
            log.append(f"Process {process.name} did not finish")

    return log, execution_order





def stcf_scheduling(processes, total_time):
    log = []
    processes_copy = list(processes)
    processes_copy.sort(key=lambda x: x.arrival_time)
    
    current_time = 0
    process_queue = []
    current_process = None
    execution_order = []

    while processes_copy or process_queue or (current_process and current_process.status > 0) or current_time < total_time: #TODO: NOTE Intervened (added "or current_time < total_time")
        while processes_copy and processes_copy[0].arrival_time <= current_time:
            arriving_process = processes_copy.pop(0)
            log.append(f"Time {current_time}: {arriving_process.name} arrived")
            process_queue.append(arriving_process)

        if not process_queue and processes_copy:
            log.append(f"Time {current_time}: Idle")
            current_time = processes_copy[0].arrival_time
            continue
        
        if not current_process and process_queue:
            process_queue.sort(key=lambda x: x.status)  # Sort process queue based on remaining execution time
            current_process = process_queue.pop(0)  # Dequeue the process with the shortest remaining time
            if current_process.start_time is None:  # If the process has not started yet, set the start time
                current_process.start_time = current_time
            log.append(f"Time {current_time}: {current_process.name} selected (burst {current_process.execution_time})")

        if current_process:
            current_process.status -= 1  # Decrease the remaining execution time of the current process
            if current_process.status == 0:  # If the process has finished executing
                current_process.finish_time = current_time + 1  # Set the finish time
                log.append(f"Time {current_process.finish_time}: {current_process.name} finished")
                execution_order.append(current_process)  # Add the process to the execution order
                current_process = None  # Reset the current process
            else:
                # Check for preemption
                if process_queue:
                    process_queue.sort(key=lambda x: x.status)
                    if process_queue[0].status < current_process.status:
                        log.append(f"Time {current_time}: {current_process.name} preempted by {process_queue[0].name}")
                        process_queue.append(current_process)
                        current_process = None

        current_time += 1

    for process in processes:
        if process.finish_time is None:
            log.append(f"Process {process.name} did not finish")

    return log, execution_order


def round_robin_scheduling(processes, total_time, time_slice):
    log = []
    processes_copy = list(processes)
    processes_copy.sort(key=lambda x: x.arrival_time)
    
    current_time = 0
    process_queue = []
    execution_order = []

    while current_time < total_time and (processes_copy or process_queue):
        while processes_copy and processes_copy[0].arrival_time <= current_time:
            arriving_process = processes_copy.pop(0)
            log.append(f"Time {current_time:>3} : {arriving_process.name} arrived")
            process_queue.append(arriving_process)

        if not process_queue and processes_copy:
            while current_time < processes_copy[0].arrival_time and current_time < total_time:    
                log.append(f"Time {current_time:>3} : Idle")
                current_time += 1
            if current_time >= total_time:
                break
            continue

        current_process = process_queue.pop(0)
        if current_process.start_time is None:
            current_process.start_time = current_time
        log.append(f"Time {current_time:>3} : {current_process.name} selected (burst {current_process.remaining_time})")

        execution_time = min(time_slice, current_process.remaining_time)  # Determine execution time based on time slice
        current_time += execution_time
        current_process.remaining_time -= execution_time  # Decrease the remaining execution time

        if current_time >= total_time:
            break
        
        if current_process.remaining_time > 0:
            while processes_copy and processes_copy[0].arrival_time <= current_time:
                arriving_process = processes_copy.pop(0)
                log.append(f"Time {current_time:>3} : {arriving_process.name} arrived")
                process_queue.append(arriving_process)
            process_queue.append(current_process)
        else:
            current_process.finish_time = current_time
            log.append(f"Time {current_time:>3} : {current_process.name} finished")
            execution_order.append(current_process)

    while current_time < total_time:
        log.append(f"Time {current_time:>3} : Idle")
        current_time += 1

    for process in processes:
        if process.finish_time is None:
            log.append(f"Process {process.name} did not finish")

    return log, execution_order





def calculate_metrics(process):
    if process.finish_time is None or process.start_time is None:
        return None, None, None  # Or some default values

    # Turnaround Time = Completion Time - Arrival Time
    turnaround_time = process.finish_time - process.arrival_time

    # Wait Time = Turnaround Time - Burst Time
    wait_time = turnaround_time - process.execution_time

    # Response Time = Start Time - Arrival Time
    response_time = process.start_time - process.arrival_time

    return wait_time, turnaround_time, response_time






if __name__ == "__main__":
    
    input_file = 'inputFile.in'
    output_file = 'inputFile.out'

    process_count, total_time, scheduling_algo, processes, time_slice = read_input_file(input_file)

    print(process_count, total_time, scheduling_algo, time_slice)
    for process in processes:
        print(process)
    
    log = [] 
    log.append(f"{process_count} processes")
    log.append(f"Using {scheduling_algo}")

    print(log)

    if scheduling_algo == 'fcfs':
        log, execution_order = fifo_scheduling(processes, total_time)
    elif scheduling_algo == 'stcf':
        log, execution_order = stcf_scheduling(processes, total_time)
    elif scheduling_algo == 'sjf':
        log, execution_order = sjf_scheduling(processes, total_time)
    elif scheduling_algo == 'rr':
        log, execution_order = round_robin_scheduling(processes, total_time, time_slice)

    print(f'Log: {log}')

    if execution_order is not None:
        for execution in execution_order:
            print(execution)
    else:
        print("No execution order available")

    # Comment out or remove the following lines:
    # for execution in execution_order:
    #     print(execution)

    write_output_file(output_file, log, processes, scheduling_algo, time_slice, total_time)
