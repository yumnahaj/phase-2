
public class Schedule implements ISchedule {

    private Map<ITimeSlot, Integer> eventsByTime;
    
    private Map<Integer, ITimeSlot> timesById;
    
    private Set<Integer> eventIds;

    public Schedule() {
        eventsByTime = new BSTMap<ITimeSlot, Integer>();
        timesById = new BSTMap<Integer, ITimeSlot>();
        eventIds = new BSTSet<Integer>();
    }


    public int size() {
        return eventIds.size();
    }


    public boolean empty() {
        return eventIds.empty();
    }

 
    public void clear() {
        eventsByTime.clear();
        timesById.clear();
        eventIds.clear();
    }


    public boolean add(int eventId, ITimeSlot timeSlot) {
        // 1. Fail if the event ID already exists
        if (eventIds.contains(eventId)) {
            return false;
        }

        // 2. Fail if there is a time conflict
        if (conflicts(timeSlot)) {
            return false;
        }
        
        // 3. If no conflicts, insert into all three BSTs
        eventIds.insert(eventId);
        eventsByTime.insert(timeSlot, eventId);
        timesById.insert(eventId, timeSlot);
        
        return true;
    }
    

    public boolean remove(int eventId) {
        // Attempt to remove the ID from the Set. If it succeeds, the event exists.
        if (eventIds.remove(eventId)) {
            // Retrieve the associated TimeSlot so we can remove it from the eventsByTime map
            ITimeSlot timeSlot = timesById.get(eventId);
            
            // Remove the remaining references
            timesById.remove(eventId);
            eventsByTime.remove(timeSlot);
            
            return true;
        }
            
        return false;    
    }
    
  
    public boolean contains(int eventId) {
        return eventIds.contains(eventId);
    }

  
    public boolean conflicts(ITimeSlot timeSlot) {

        return (eventsByTime.get(timeSlot) != null);
    }


    public Set<Integer> getEventIds() {
        return eventIds;
    }


    public Map<ITimeSlot, Integer> getEvents() {
        return eventsByTime;
    }
}
