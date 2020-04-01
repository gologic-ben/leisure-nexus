package com.leisurenexus.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.leisurenexus.api.interest.Interest;
import com.leisurenexus.api.interest.InterestType;
import com.leisurenexus.api.recommandation.Recommandation;
import com.leisurenexus.api.user.User;
import com.leisurenexus.api.user.UserNotFoundException;

@RestController
public class CytoscapeController {
  private static Logger LOG = LoggerFactory.getLogger(CytoscapeController.class);

  private @Autowired com.leisurenexus.api.user.UserRepository userRepository;

  /**
   * elements : { nodes : [ { data : { id : 'cat', name : 'Cat', type: 'user' } }, { data : { id :
   * 'bird', name : 'Bird', type: 'interest' } } ], edges : [ { data : { source : 'cat', target :
   * 'bird' } ] },
   * @param id
   * @return
   * @throws UserNotFoundException
   */
  @GetMapping("/cytoscape/users/{id}")
  public Map<String, List<Object>> getUser(@PathVariable("id") Long id) throws UserNotFoundException {
    LOG.info("cytoscape of {}", id);

    Map<String, List<Object>> elements = new HashMap<String, List<Object>>();
    elements.put("nodes", new ArrayList<Object>());
    elements.put("edges", new ArrayList<Object>());

    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      User u = user.get();
      addNode(elements, u.getId().toString(), user.get().getName(), u.getClass());
      // for each recommandations create a node and a edge
      for (Recommandation r : u.getRecommandations()) {
        addNode(elements, r.getId().toString(), r.getName(), r.getClass());
        addEdge(elements, u.getId().toString(), r.getId().toString());
      }
      // foreach interest types create a node and a edge
      for (InterestType type : InterestType.values()) {
        LOG.info("InterestType {}", type);        
        String interestId = type.name() + "-" + u.getId().toString();
        addNode(elements, interestId, type.name(), type.getClass());
        addEdge(elements, u.getId().toString(), interestId);

        // foreach interests of "type", create a node and a edge
        for (Interest i : u.getInterests()) {
          LOG.info("Interest {}", i);
          if (i.getType().equals(type)) {
            User s = i.getSource();
            // create a node and a edge for source link to interests type
            addNode(elements, s.getId().toString(), s.getName()+"-"+s.getId(), u.getClass());
            addEdge(elements, interestId, s.getId().toString());

            // for each source's recommendation of "type" create a node and a edge
            for (Recommandation rs : s.getRecommandations()) {
              LOG.info("Recommandation {}", rs);
              if(rs.getClass().equals(type.getRecommandationClass())) {
                addNode(elements, rs.getId().toString(), rs.getName(), rs.getClass());
                addEdge(elements, s.getId().toString(), rs.getId().toString());
              }
            }

          }
        }
      }
      return elements;

    }
    throw new UserNotFoundException();
  }

  private void addNode(Map<String, List<Object>> parent, String id, String name, Class<?> type) {
    Map<String, String> data = new HashMap<String, String>();
    data.put("id", id);
    data.put("name", name);
    data.put("type", type.getSimpleName());

    Map<String, Map<String, String>> o = new HashMap<String, Map<String, String>>();
    o.put("data", data);

    parent.get("nodes").add(o);

  }

  private void addEdge(Map<String, List<Object>> parent, String sourceId, String targetId) {
    Map<String, String> data = new HashMap<String, String>();
    data.put("source", sourceId);
    data.put("target", targetId);

    Map<String, Map<String, String>> o = new HashMap<String, Map<String, String>>();
    o.put("data", data);

    parent.get("edges").add(o);
  }
}
