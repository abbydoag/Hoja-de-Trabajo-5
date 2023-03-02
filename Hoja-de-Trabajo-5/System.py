import simpy
import random

# Especificaciones
RANDOM_SEED = 42
RAM_MEMORY = 100
INTERVAL = 10
INSTRUCTIONS_TIME = 3
TIME_I_O = 3
TIME_CPU = 1
random.seed(RANDOM_SEED)

class Process:
    def __init__(self, env, name, cpu, ram_memory):
        self.env=env
        self.name=name
        self.cpu=cpu
        self.ram_memory=ram_memory
        self.requiredMem=random.randint(1, 10)
        self.instructionTotal=random.randint(1, 10)
        self.instructionLeft=self.instructionTotal
        self.state="NEW"
        self.queueTime=0
        self.timeBeginning=0
        self.timeEnd=0
        self.wait=False

    def __repr__(self):
        return self.name
    
    def runProgram(self):
        while self.instructionLeft > 0:
            with self.cpu.request() as request:
                self.state="READY"
                yield request
                self.state="RUNNING"
                executedInstructions=min(INSTRUCTIONS_TIME, self.instructionLeft)
                yield self.env.timeout(TIME_CPU * executedInstructions)
                self.instructionLeft-=executedInstructions
                if self.instructionLeft<=0:
                    self.finishProcess()
                    return
                if random.randint(1, 21)==1:
                    self.state="WAITING"
                    yield self.env.timeout(TIME_I_O)
                    self.state="READY"
                elif random.randint(1, 21)==2:
                    self.state="READY"
    
    def finishProcess(self):
        self.state="TERMINATED"
        self.timeEnd=self.env.now
        totalTime=self.timeEnd - self.timeBeginning
        print(f"{self.name} finalizó a las {totalTime:.2f} unidades de tiempo")
        self.releaseMemory()

    def requireMemory(self):
        self.state="...WAITING FOR MEMORY..."
        yield self.ram_memory.get(self.requiredMem)
        self.state="READY"
        self.queueTime=self.env.now-self.timeBeginning
        print(f"{self.env.now:.2f}: {self.name} recibió {self.requiredMem} de memoria RAM")
    
    def releaseMemory(self):
        yield self.ram_memory.put(self.requiredMem)
        print(f"{self.env.now:.2f}: {self.name} liberó {self.requiredMem} de memoria RAM")

    def startProcess(self):
        self.timeBeginning=self.env.now
        print(f"{self.env.now:.2f}: {self.name} entró al sistema con una memoria de {self.requiredMem}.")
        yield self.env.process(self.requireMemory())
        self.state="READY"
        yield self.env.process(self.runProgram())

class CPU:
    def __init__(self, env, num_cpus):
        self.env=env
        self.cpu=simpy.Resource(env, capacity=num_cpus)
        self.num_cpus=num_cpus
    
    def run(self, process):
        self.env.process(process.startProcess())

def generateProcesses(env, cpu, ram_memory, interval):
    i=0
    while True:
        i+=1
        process=Process(env, f"Proceso {i}", cpu, ram_memory)
        env.process(process.runProgram())
        yield env.timeout(interval)

#SIMULACION
env = simpy.Environment()
cpu = CPU(env, 1)
ram_memory = simpy.Container(env, init=RAM_MEMORY )