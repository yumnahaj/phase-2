/**
 * A schedule storing non-conflicting events identified by their IDs and time slots.
 * This class uses Binary Search Tree (BST) implementations of Map and Set to 
 * ensure that all core operations run in O(log n) time on average.
 */
public class Schedule implements ISchedule {

    // Maps a TimeSlot to its Event ID (used for conflict checking)
    private Map<ITimeSlot, Integer> eventsByTime;
    
    // Maps an Event ID to its TimeSlot (used for fast O(log n) removals)
    private Map<Integer, ITimeSlot> timesById;
    
    // A Set containing all scheduled Event IDs (used for fast O(log n) lookups)
    private Set<Integer> eventIds;

    public Schedule() {
        // Assuming your friends will name their BST implementations BSTMap and BSTSet
        // If they use different names, you can easily change them here!
        eventsByTime = new BSTMap<ITimeSlot, Integer>();
        timesById = new BSTMap<Integer, ITimeSlot>();
        eventIds = new BSTSet<Integer>();
    }

    /**
     * Returns the number of events in the schedule.
     */
    @Override
    public int size() {
        return eventIds.size();
    }

    /**
     * Returns true if the schedule is empty.
     * Must run in O(1).
     */
    @Override
    public boolean empty() {
        return eventIds.empty();
    }

    /**
     * Removes all events from the schedule.
     */
    @Override
    public void clear() {
        eventsByTime.clear();
        timesById.clear();
        eventIds.clear();
    }

    /**
     * Inserts a new event into the schedule.
     *
     * Insertion fails if:
     * - the event ID already exists in the schedule, or
     * - the time slot conflicts with an existing event.
     *
     * @param eventId  the event ID to insert
     * @param timeSlot the time slot of the event
     * @return true if inserted, false otherwise
     *
     * Must run in O(log n) on average.
     */
    @Override
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
    
    /**
     * Removes the given event ID if it exists.
     *
     * @param eventId the event ID to remove
     * @return true if removed, false otherwise
     *
     * Must run in O(log n) on average.
     */
    @Override
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
    
    /**
     * Checks whether the given event ID exists in the schedule.
     *
     * @param eventId the event ID to search for
     * @return true if the event ID exists, false otherwise
     *
     * Must run in O(log n) on average.
     */
    @Override
    public boolean contains(int eventId) {
        return eventIds.contains(eventId);
    }

    /**
     * Checks whether the given time slot conflicts with an existing event.
     * * Because ITimeSlot's compareTo() returns 0 for overlapping times, 
     * the BST Map will treat overlaps as duplicate keys!
     *
     * @param timeSlot the time slot to test
     * @return true if a conflict exists, false otherwise
     *
     * Must run in O(log n) on average.
     */
    @Override
    public boolean conflicts(ITimeSlot timeSlot) {
        // If getting the time slot returns a value, it means a matching (overlapping)
        // time slot already exists in the BST!
        return (eventsByTime.get(timeSlot) != null);
    }

    /**
     * Returns all event IDs stored in the schedule.
     *
     * @return the set of event IDs
     */
    @Override
    public Set<Integer> getEventIds() {
        return eventIds;
    }

    /**
     * Returns all scheduled events indexed by their time slots.
     *
     * @return a map from time slots to event IDs
     */
    @Override
    public Map<ITimeSlot, Integer> getEvents() {
        return eventsByTime;
    }
}